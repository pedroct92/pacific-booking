package com.challenge.pedrotorres.pacificbooking.domain.campsite;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@Entity
@SequenceGenerator(name = "site_seq")
@DataObject(generateConverter = true)
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "site_seq")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Version
    private Long version;

    public Site() {

    }

    public Site(JsonObject jsonObject) {
        SiteConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        SiteConverter.toJson(this, json);
        return json;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
