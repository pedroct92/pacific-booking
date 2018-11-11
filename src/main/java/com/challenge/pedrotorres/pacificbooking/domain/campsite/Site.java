package com.challenge.pedrotorres.pacificbooking.domain.campsite;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

@Entity
@DataObject(generateConverter = true)
public class Site {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "site_id")
    private List<SiteAvailability> siteAvailabilities;

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

    public List<SiteAvailability> getSiteAvailabilities() {
        return siteAvailabilities;
    }

    public void setSiteAvailabilities(List<SiteAvailability> siteAvailabilities) {
        this.siteAvailabilities = siteAvailabilities;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
