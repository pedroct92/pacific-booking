package com.challenge.pedrotorres.pacificbooking.api.responses;

import com.challenge.pedrotorres.pacificbooking.api.responses.ResponseConverter;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

@DataObject(generateConverter = true)
public class Response {

    @GenIgnore
    private Object results;
    private List<String> errors;
    private Integer status;

    private Response(Object results, List<String> errors, Integer status) {
        this.results = results;
        this.errors = errors;
        this.status = status;
    }

    public Response(JsonObject jsonObject) {
        this.results = jsonObject.getValue("results");
        ResponseConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();

        if(this.results != null && !(this.results instanceof List)) { // FIXME
            json.put("results", new JsonObject(Json.encode(this.results)));
        } else {
            json.put("results", this.results);
        }
        ResponseConverter.toJson(this, json);
        return json;
    }

    public Object getResults() {
        return results;
    }

    public void setResults(Object results) {
        this.results = results;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static class ResponseBuilder {

        private Object results;
        private List<String> errors;
        private Integer status;

        public ResponseBuilder results(Object results) {
            this.results = results;
            return this;
        }

        public ResponseBuilder addError(String error) {
            if(this.errors == null) {
                this.errors = new ArrayList<>();
            }
            this.errors.add(error);
            return this;
        }

        public ResponseBuilder withStatus(Integer status) {
            this.status = status;
            return this;
        }

        public Boolean hasErrors() {
            return this.errors != null && !this.errors.isEmpty();
        }

        public Response build() {
            return new Response(results, errors, status);
        }
    }
}
