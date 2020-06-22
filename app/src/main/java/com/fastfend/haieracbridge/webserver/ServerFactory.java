package com.fastfend.haieracbridge.webserver;

import com.fastfend.haieracbridge.haierapi.ACDevice;
import com.fastfend.haieracbridge.haierapi.ACFanSpeed;
import com.fastfend.haieracbridge.haierapi.ACMode;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;

import org.restlet.data.Status;
import org.restlet.engine.Engine;
import org.restlet.engine.adapter.HttpServerHelper;
import org.restlet.ext.gson.GsonConverter;
import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.ext.json.JsonRepresentation;

import java.util.List;

public class ServerFactory {
    private static String Token = "";

    static {
        Engine.getInstance().getRegisteredServers().add(new HttpServerHelper());
        Engine.getInstance().getRegisteredConverters().add(new GsonConverter());
    }

    private static List<ACDevice> Devices;

    private static Restlet deviceData = new Restlet() {
        @Override
        public void handle(Request request, Response response) {
            if(request.getMethod() == Method.POST || request.getMethod() == Method.GET)
            {
                String token = request.getHeaders().getValues("token");
                String deviceid = request.getHeaders().getValues("id");

                if(token != null)
                {
                    if(deviceid != null)
                    {
                        if(token.equals(Token))
                        {
                            if(request.getMethod() == Method.GET)
                            {
                                for (ACDevice device : Devices) {
                                    if (deviceid.equals(device.getDeviceID())) {
                                        JsonObject object = new JsonObject();

                                        object.addProperty("id", device.getDeviceID());
                                        object.addProperty("powerstate", device.getPowerState());
                                        object.addProperty("temp", device.getIndoorTemp());
                                        object.addProperty("tempset", device.getSetTemp());
                                        object.addProperty("humidity", device.getHumidity());
                                        object.addProperty("mode", device.getMode().name());
                                        object.addProperty("fanspeed", device.getFanSpeed().name());
                                        object.addProperty("safefan", device.getEcoSensor());
                                        object.addProperty("swing_rl", device.getVerticalSway());
                                        object.addProperty("swing_ud", device.getHorizontalSway());
                                        object.addProperty("healthmode", device.getHealthMode());

                                        GsonRepresentation rep = new GsonRepresentation<>(object);
                                        response.setEntity(rep);
                                        break;
                                    }
                                }
                            }
                            else
                            {
                                try
                                {
                                    boolean found = false;
                                    for (ACDevice device : Devices) {
                                        if (deviceid.equals(device.getDeviceID())) {
                                            found = true;
                                            JSONObject resp = new JsonRepresentation(request.getEntity()).getJsonObject();
                                            JsonParser jsonParser = new JsonParser();
                                            JsonObject object = (JsonObject)jsonParser.parse(resp.toString());

                                            if(object.has("powerstate"))
                                            {
                                                boolean val = object.get("powerstate").getAsBoolean();
                                                if(device.getPowerState() != val)
                                                {
                                                    device.SetPower(val);
                                                }
                                            }
                                            if(object.has("tempset"))
                                            {
                                                int val = object.get("tempset").getAsInt();
                                                if(device.getSetTemp() != val)
                                                {
                                                    device.SetTargetTemperature(val);
                                                }
                                            }
                                            if(object.has("mode"))
                                            {
                                                String val = object.get("mode").getAsString();
                                                ACMode mode = ACMode.valueOf(val);
                                                if(device.getMode() != mode)
                                                {
                                                    device.SetMode(mode);
                                                }
                                            }
                                            if(object.has("fanspeed"))
                                            {
                                                String val = object.get("fanspeed").getAsString();
                                                ACFanSpeed mode = ACFanSpeed.valueOf(val);
                                                if(device.getFanSpeed() != mode)
                                                {
                                                    device.SetFanSpeed(mode);
                                                }
                                            }
                                            if(object.has("swing_rl"))
                                            {
                                                boolean val = object.get("swing_rl").getAsBoolean();
                                                if(device.getVerticalSway() != val)
                                                {
                                                    device.SetVerticalSway(val);
                                                }
                                            }
                                            if(object.has("swing_ud"))
                                            {
                                                boolean val = object.get("swing_ud").getAsBoolean();
                                                if(device.getHorizontalSway() != val)
                                                {
                                                    device.SetHorizontalSway(val);
                                                }
                                            }
                                            if(object.has("healthmode"))
                                            {
                                                boolean val = object.get("healthmode").getAsBoolean();
                                                if(device.getHealthMode() != val)
                                                {
                                                    device.SetHealthMode(val);
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    if (found != true)
                                    {
                                        response.setStatus(new Status(Status.SERVER_ERROR_INTERNAL, "No device with provided id found"));
                                        response.setEntity("No device with provided id found", MediaType.TEXT_HTML);
                                    }
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                    response.setStatus(new Status(Status.SERVER_ERROR_INTERNAL, "Error during settings data"));
                                    response.setEntity("Error during settings data", MediaType.TEXT_HTML);
                                }
                            }
                        }
                        else
                        {
                            response.setStatus(new Status(Status.CLIENT_ERROR_FORBIDDEN, "Wrong token"));
                            response.setEntity("Wrong token", MediaType.TEXT_HTML);
                        }
                    }
                    else
                    {
                        response.setStatus(new Status(Status.CLIENT_ERROR_BAD_REQUEST, "Missing id header"));
                        response.setEntity("Missing id header", MediaType.TEXT_HTML);
                    }
                }
                else
                {
                    response.setStatus(new Status(Status.CLIENT_ERROR_BAD_REQUEST, "Missing token header"));
                    response.setEntity("Missing token header", MediaType.TEXT_HTML);
                }
            }
            else
            {
                response.setStatus(new Status(Status.CLIENT_ERROR_BAD_REQUEST, "Accepting only GET and POST method"));
                response.setEntity("Accepting only GET and POST method", MediaType.TEXT_HTML);
            }
        }
    };

    private static Restlet listDevices = new Restlet() {
        @Override
        public void handle(Request request, Response response) {
            if(request.getMethod() == Method.GET)
            {
                String token = request.getHeaders().getValues("token");
                if(token != null) {
                    if (token.equals(Token))
                    {
                        JsonArray array = new JsonArray();
                        for (ACDevice dev : Devices)
                        {
                            JsonObject object = new JsonObject();
                            object.addProperty("id", dev.getDeviceID());
                            object.addProperty("name", dev.getName());
                            array.add(object);
                        }

                        GsonRepresentation rep = new GsonRepresentation<>(array);
                        response.setEntity(rep);
                    }
                    else {
                        response.setStatus(new Status(Status.CLIENT_ERROR_FORBIDDEN, "Wrong token"));
                        response.setEntity("Wrong token", MediaType.TEXT_HTML);
                    }
                }
                else
                {
                    response.setStatus(new Status(Status.CLIENT_ERROR_BAD_REQUEST, "Missing token header"));
                    response.setEntity("Missing token header", MediaType.TEXT_HTML);
                }
            }
            else
            {
                response.setStatus(new Status(Status.CLIENT_ERROR_BAD_REQUEST, "Accepting only GET method"));
                response.setEntity("Accepting only GET method", MediaType.TEXT_HTML);
            }
        }
    };

    public static Server createServer(List<ACDevice> devices, String token) {
        Component component = new Component();
        Devices = devices;
        Token = token;
        Server server = component.getServers().add(Protocol.HTTP, 10000);

        component.getDefaultHost().attach("/listDevices", listDevices);
        component.getDefaultHost().attach("/deviceData", deviceData);

        return server;
    }
}
