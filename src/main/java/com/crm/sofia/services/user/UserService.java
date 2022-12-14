package com.crm.sofia.services.user;

import com.crm.sofia.config.AppConstants;
import com.crm.sofia.dto.user.*;
import com.crm.sofia.exception.OAuth2AuthenticationProcessingException;
import com.crm.sofia.exception.UserAlreadyExistAuthenticationException;
import com.crm.sofia.exception.login.IncorrectPasswordException;
import com.crm.sofia.exception.login.UserNotFoundException;
import com.crm.sofia.mapper.user.UserMapper;
import com.crm.sofia.model.user.LocalUser;
import com.crm.sofia.model.user.Role;
import com.crm.sofia.model.user.User;
import com.crm.sofia.repository.user.RoleRepository;
import com.crm.sofia.repository.user.UserRepository;
import com.crm.sofia.security.jwt.TokenProvider;
import com.crm.sofia.security.oauth2.user.OAuth2UserInfo;
import com.crm.sofia.security.oauth2.user.OAuth2UserInfoFactory;
import com.crm.sofia.services.auth.JWTService;
import com.crm.sofia.services.menu.MenuFieldService;
import com.crm.sofia.utils.GeneralUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.*;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final EntityManager entityManager;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder,
                       JWTService jwtService,
                       MenuFieldService menuFieldService,
                       RoleRepository roleRepository,
                       AuthenticationManager authenticationManager,
                       TokenProvider tokenProvider,
                       EntityManager entityManager) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.entityManager = entityManager;
    }

    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = userRepository.getAllUsers(AppConstants.Types.UserStatus.deleted);
        return users;
    }

    public UserDTO getUser(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userMapper.mapUserToDto(userOptional.get());
        } else {
            return null;
        }
    }

    public UserDTO getTransferUser(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userMapper.map(userOptional.get());
        } else {
            return null;
        }
    }

    public UserDTO postUser(UserDTO userDTO) {

        if (userDTO.getPassword().equals("") || !userDTO.getPassword().equals(userDTO.getRepeatPassword())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Password error!!");
        }

        User user = userMapper.map(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        boolean existingUser = userRepository.userExists(userDTO.getUsername());
        if (existingUser) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User already exists!!");
        }

        user.setCreatedBy(this.jwtService.getUserId());
        user.setCreatedOn(Instant.now());
        user.setModifiedBy(this.jwtService.getUserId());
        user.setModifiedOn(Instant.now());
        user.setEnabled(true);
        user.setCurrentLanguage(user.getDefaultLanguage());
        User createdUser = userRepository.save(user);
        UserDTO responseUserDTO = userMapper.map(createdUser);
        responseUserDTO.setPassword("");

        return responseUserDTO;
    }

    public void postTransferUser(UserDTO userDTO) {
        User user = userMapper.map(userDTO);
        User createdUser = userRepository.save(user);
    }

    @Transactional
    public ResponseEntity<?> authenticate(@NotBlank String username, @NotBlank String enteredPassword) {

        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        if (passwordEncoder.matches(enteredPassword, user.getPassword())) {
            user.setEnabled(true);
            UserDetails userDetails =
                    new LocalUser(user.getEmail(), user.getPassword(),
                            true, true, true,
                            true,
                            GeneralUtils.buildSimpleGrantedAuthorities(user.getRolesSet()), user, user.getRoles());


            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, enteredPassword)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.createToken(authentication);
            LocalUser localUser = (LocalUser) authentication.getPrincipal();

            UserDTO userDTO = this.userMapper.mapUserToDtoWithMenu(localUser.getUser());
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, userDTO));
        } else {

            throw new IncorrectPasswordException();
        }
    }

    @Transactional
    public void updateCurrentLanguage(Long languageId) {
        String userId = this.jwtService.getUserId();

       this.userRepository.updateCurrentLanguage(userId, languageId);
    }

    public UserDTO putUser(UserDTO userDTO) {
        User user = userMapper.map(userDTO);
        if ((userDTO.getPassword()==null?"":userDTO.getPassword()).equals("") && (userDTO.getRepeatPassword()==null?"":userDTO.getRepeatPassword()).equals("")) {
            String password = userRepository.findPasswordById(user.getId());
            user.setPassword(password);
        } else if (!userDTO.getPassword().equals(userDTO.getRepeatPassword())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Password error!!");
        } else {
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        user.setCreatedBy(this.jwtService.getUserId());
        user.setCreatedOn(Instant.now());
        user.setModifiedBy(this.jwtService.getUserId());
        user.setModifiedOn(Instant.now());
        user.setEnabled(true);
        user.setCurrentLanguage(user.getDefaultLanguage());
        User createdUser = userRepository.save(user);

        UserDTO responseUserDTO = userMapper.map(createdUser);
        responseUserDTO.setPassword("");

        return responseUserDTO;
    }

    public Boolean delete(String id) {
        userRepository.deleteById(id);
        return true;
    }

    public UserDTO getCurrentUser() {
        User user = this.getLoggedInUser();
        UserDTO userDTO = this.userMapper.mapUserToDtoWithMenu(user);
        return userDTO;
    }

    public User getLoggedInUser() {
        String id = jwtService.getUserId();
        Optional<User> userOptional = this.userRepository.findById(id);
        if (!userOptional.isPresent()) {
            return null;
        }
        return userOptional.get();
    }

    @Transactional
    public User registerThirdPartyUser(final SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException {
        User user = buildUser(signUpRequest);

        user.setCreatedOn(Instant.now());
        user.setModifiedOn(Instant.now());
        user.setEnabled(true);
        user = userRepository.save(user);
        userRepository.initiateUserInfo(user.getId());
        return user;
    }

    public void nativeQueryTest(){

        String queryString =
                " SELECT " +
                        " ac.create_entity, " +
                        " ac.delete_entity, " +
                        " ac.read_entity, " +
                        " ac.update_entity " +
                        " FROM access_control ac " +
                        " WHERE ac.type = :type " +
                        " AND ac.entity_id = :entityId " +
                        " AND ac.role_id IN ( SELECT role_id FROM user_role WHERE user_id = :userId);   ";

        Query query = entityManager.createNativeQuery(queryString);

//        query.setParameter("type", type);
//        query.setParameter("entityId", entityId);
//        query.setParameter("userId", userId);

        List<Object[]> fields = query.getResultList();

        Boolean createEntity = false;
        Boolean deleteEntity = false;
        Boolean readEntity   = false;
        Boolean updateEntity = false;

        for (Object[] field : fields) {
            Boolean createEntityRow = (Boolean) field[0];
            Boolean deleteEntityRow = (Boolean) field[1];
            Boolean readEntityRow   = (Boolean) field[2];
            Boolean updateEntityRow = (Boolean) field[3];

            if(createEntityRow) createEntity = true;
            if(deleteEntityRow) deleteEntity = true;
            if(readEntityRow) readEntity = true;
            if(updateEntityRow) updateEntity = true;
        }
    }

    private User buildUser(final SignUpRequest formDTO) {
        User user = new User();
        user.setUsername(formDTO.getDisplayName());
        user.setEmail(formDTO.getEmail());
        user.setPassword(passwordEncoder.encode(formDTO.getPassword()));
        final HashSet<Role> roles = new HashSet<Role>();
      //  roles.add(roleRepository.findFirstByName(Role.ROLE_USER));
        user.setRolesSet(roles);
        user.setProvider(formDTO.getSocialProvider().getProviderType());

        user.setEnabled(true);
        user.setProviderUserId(formDTO.getProviderUserId());
        return user;
    }

    public User findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByProviderId(final String provider, final String providerId) {
        return userRepository.findByProviderAndProviderUserId(provider, providerId);
    }

    @Transactional
    public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) throws UserAlreadyExistAuthenticationException {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);
        SignUpRequest userDetails = toUserRegistrationObject(registrationId, oAuth2UserInfo);
      //  User user = findUserByProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getId());
        User user = findUserByUsername(oAuth2UserInfo.getName());
        if (user != null) {
            if (!user.getProvider().equals(registrationId) && !user.getProvider().equals(SocialProvider.LOCAL.getProviderType())) {
                throw new OAuth2AuthenticationProcessingException(
                        "Looks like you're signed up with " + user.getProvider() + " account. Please use your " + user.getProvider() + " account to login.");
            }
        } else {
            user = registerThirdPartyUser(userDetails);
        }
        return LocalUser.create(user, attributes, idToken, userInfo);
    }

    private SignUpRequest toUserRegistrationObject(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
        byte[] array = new byte[10]; // length is bounded by 7
        new Random().nextBytes(array);
        String passwordString = new String(array, Charset.forName("UTF-8"));

        return SignUpRequest.getBuilder()
                .addProviderUserID(oAuth2UserInfo.getId())
                .addDisplayName(oAuth2UserInfo.getName())
                .addEmail(oAuth2UserInfo.getEmail())
                .addSocialProvider(GeneralUtils.toSocialProvider(registrationId)).addPassword(passwordString).build();
    }

    public Optional<User> findUserById(String id) {
        return userRepository.findById(id);
    }

    public User findUserByUsername(final String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()){
            return null;
        } else {
            return optionalUser.get();
        }
    }

}
