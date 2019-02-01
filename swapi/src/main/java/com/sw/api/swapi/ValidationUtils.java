package com.sw.api.swapi;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;

 
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
 
public class ValidationUtils {
    
    public static final String JSON_V4_SCHEMA_IDENTIFIER = "http://json-schema.org/draft-04/schema#";
    public static final String JSON_SCHEMA_IDENTIFIER_ELEMENT = "$schema";
    
    public static JsonNode getJsonNode(String jsonText) 
    throws IOException
    {
        return JsonLoader.fromString(jsonText);
    } // getJsonNode(text) ends
    
  
    public static JsonSchema getSchemaNode(String schemaText)
    throws IOException, ProcessingException 
    {
        final JsonNode schemaNode = getJsonNode(schemaText);
        return _getSchemaNode(schemaNode);
    } 
    
    public static void validateJson(JsonSchema jsonSchemaNode, JsonNode jsonNode)
    throws ProcessingException 
    {
        ProcessingReport report = jsonSchemaNode.validate(jsonNode);
        if (!report.isSuccess()) {
            for (ProcessingMessage processingMessage : report) {
                throw new ProcessingException(processingMessage);
            }
        }
    } // validateJson(Node) ends
    
    public static boolean isJsonValid(JsonSchema jsonSchemaNode, JsonNode jsonNode) throws ProcessingException
    {
        ProcessingReport report = jsonSchemaNode.validate(jsonNode);
        return report.isSuccess();
    } // validateJson(Node) ends
    
    /**
     * Validating the Json Schema with the output of API
     * @param schemaText
     * @param jsonText
     * @return
     * @throws ProcessingException
     * @throws IOException
     */
    public static boolean isJsonSchemaValid(String schemaText, String jsonText) throws ProcessingException, IOException
    {   
        final JsonSchema schemaNode = getSchemaNode(schemaText);
        final JsonNode jsonNode = getJsonNode(jsonText);
        return isJsonValid(schemaNode, jsonNode);
    } // validateJson(Node) ends
 
  
    private static JsonSchema _getSchemaNode(JsonNode jsonNode)
    throws ProcessingException
    {
        final JsonNode schemaIdentifier = jsonNode.get(JSON_SCHEMA_IDENTIFIER_ELEMENT);
        if (null == schemaIdentifier){
            ((ObjectNode) jsonNode).put(JSON_SCHEMA_IDENTIFIER_ELEMENT, JSON_V4_SCHEMA_IDENTIFIER);
        }
        
        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        return factory.getJsonSchema(jsonNode);
    } // _getSchemaNode() ends
    
    
    /**
     * reading file as a string
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String readFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }
}