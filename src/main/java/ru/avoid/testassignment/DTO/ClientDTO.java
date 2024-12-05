package ru.avoid.testassignment.DTO;

import jakarta.validation.constraints.Size;
import lombok.Builder;

public class ClientDTO {

    @Size(min = 2, max = 50, message = "name isn't valid")
    private String name;

    @Size(min = 2, max = 20, message = "short_name isn't valid")
    private String shortName;

    @Size(min = 2, max = 50, message = "address isn't valid")
    private String address;

    private String legalFormName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getLegalFormName() {
        return legalFormName;
    }

    public void setLegalFormName(String legalFormName) {
        this.legalFormName = legalFormName;
    }
}
