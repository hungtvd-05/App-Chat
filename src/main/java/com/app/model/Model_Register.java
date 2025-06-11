package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Model_Register {

    private String userName;
    private String password;
    private String fullName;
    private String mail;
    private String phone;

    public JSONObject toJSONObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("userName", userName);
            json.put("password", password);
            json.put("fullName", fullName);
            json.put("mail", mail);
            json.put("phone", phone);
            return json;
        } catch (JSONException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
