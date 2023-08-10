package com.crm.sofia.services.html_dashboard;

import com.crm.sofia.model.html_dashboard.HtmlDashboard;
import com.crm.sofia.utils.JSMin;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

@Service
public class HtmlDashboardJavascriptService {

    public String generateDynamicScript(HtmlDashboard htmlDashboard) {

        List<String> nativeHandlerLines = new ArrayList<>();

        String classInitString = this.generateClassInit(htmlDashboard);
        nativeHandlerLines.add(classInitString);

        String pointerVars = generatePointerVars();
        nativeHandlerLines.add(pointerVars);

        String userScriptsString = this.generateUserScripts(htmlDashboard);
        nativeHandlerLines.add(userScriptsString);

        String nativeFormEventsHandlerString = this.generateNativeListEventsHandler();
        nativeHandlerLines.add(nativeFormEventsHandlerString);

        String classEndString = this.generateClassEnd();
        nativeHandlerLines.add(classEndString);

        return String.join("\n", nativeHandlerLines);
    }

    private String generateNativeListEventsHandler() {
        List<String> nativeFormEventsHandler = new ArrayList<>();
        nativeFormEventsHandler.add("");
        nativeFormEventsHandler.add("nativeListEventsHandler(type, metadata) {");

        nativeFormEventsHandler.
                add("if((type == 'onHtmlDashboardOpen') && " +
                        "(typeof this.onHtmlDashboardOpen == \"function\")) " +
                        "this.onHtmlDashboardOpen(metadata);");

        nativeFormEventsHandler.
                add("if((type == 'onHtmlDashboardDataLoaded') && " +
                        "(typeof this.onHtmlDashboardDataLoaded == \"function\")) " +
                        "this.onHtmlDashboardDataLoaded(metadata);");

        nativeFormEventsHandler.add("}");

        nativeFormEventsHandler.add("");
        nativeFormEventsHandler.add("nativeAreaClickHandler(classList) {");
        nativeFormEventsHandler.
                add("if((typeof this.areaClick == \"function\")) " +
                        "this.areaClick(classList);");
        nativeFormEventsHandler.add("}");

        return String.join("\n", nativeFormEventsHandler);
    }

    private String generateUserScripts(HtmlDashboard htmlDashboard) {
        List<String> scripts = new ArrayList<>();
        htmlDashboard.getScripts().forEach(listScript -> {

            scripts.add(listScript.getScript());
        });


        return String.join("\n", scripts);
    }

    private String generateClassInit(HtmlDashboard htmlDashboard) {
        List<String> classLines = new ArrayList<>();

        List<String> classDefLines = new ArrayList<>();
        classDefLines.add("class HtmlDashboardDynamicScript");
        classDefLines.add(htmlDashboard.getId().replace("-", "_"));
        classDefLines.add(" {");
        String classDef = String.join("", classDefLines);

        String constructor = "constructor() {}";

        classLines.add(classDef);
        classLines.add(constructor);

        return String.join("\n", classLines);
    }

    private String generateClassEnd() {
        return "}";
    }

    private String generatePointerVars() {
        List<String> pointerVarLines = new ArrayList<>();
        pointerVarLines.add("");
        pointerVarLines.add("htmlDashboardRef = null;");
        pointerVarLines.add("");
//        pointerVarLines.add("navigateRef = null;");
//        pointerVarLines.add("getFromBackendRef = null;");
//        pointerVarLines.add("");
//        pointerVarLines.add("defineHtmlDashboardNavigator(myCallback){this.navigateRef = myCallback;}");
//        pointerVarLines.add("navigate(command){return this.navigateRef(command);}");

//        pointerVarLines.add("defineGetFromBackend(myCallback){this.getFromBackendRef = myCallback;}");
//        pointerVarLines.add("getFromBackend(url, callback){return this.getFromBackendRef(url, callback);}");

//        pointerVarLines.add("defineGetFromUrl(myCallback){this.getFromUrlRef = myCallback;}");
//        pointerVarLines.add("getFromUrl(url, callback){return this.getFromUrlRef(url, callback);}");

//        pointerVarLines.add("");
        return String.join("\n", pointerVarLines);
    }

    public String minify(String script) throws Exception {
        Reader reader = new StringReader(script);
        Writer writer = new StringWriter();
        JSMin jsMin = new JSMin(reader, writer);
        jsMin.jsmin();
        String scriptMin = writer.toString();
        return scriptMin;
    }

}
