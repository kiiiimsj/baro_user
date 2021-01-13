package com.tpn.baro.JsonParsingHelper;

import java.util.ArrayList;

public class EventHelperClass {
    public boolean result;
    public String message;
    public ArrayList<EventHelperParsingClass> event;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<EventHelperParsingClass> getEvent() {
        return event;
    }

    public void setEvent(ArrayList<EventHelperParsingClass> event) {
        this.event = event;
    }

    public class EventHelperParsingClass {
        public int event_id;
        public String event_image;

        public int getEvent_id() {
            return event_id;
        }

        public void setEvent_id(int event_id) {
            this.event_id = event_id;
        }

        public String getEvent_image() {
            return event_image;
        }

        public void setEvent_image(String event_image) {
            this.event_image = event_image;
        }
    }
}
