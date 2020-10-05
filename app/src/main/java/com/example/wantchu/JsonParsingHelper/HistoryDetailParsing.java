package com.example.wantchu.JsonParsingHelper;

import java.util.ArrayList;

public class HistoryDetailParsing {
    Boolean result;
    ArrayList<HistoryDetailParsingHelper> orders;
    String message;
    String requests;

    public HistoryDetailParsing(Boolean result, ArrayList<HistoryDetailParsingHelper> orders, String message) {
        this.result = result;
        this.orders = orders;
        this.message = message;
    }

    public String getRequests() {
        return requests;
    }

    public Boolean getResult() {
        return result;
    }

    public ArrayList<HistoryDetailParsingHelper> getOrders() {
        return orders;
    }

    public String getMessage() {
        return message;
    }

    public class HistoryDetailParsingHelper {
        int order_count;
        String menu_name;
        int menu_defaultprice;
        ArrayList<extras> extras;

        public HistoryDetailParsingHelper(int order_count,String menu_name, int menu_defaultprice, ArrayList<extras> extras) {
            this.order_count = order_count;
            this.menu_name = menu_name;
            this.menu_defaultprice = menu_defaultprice;
            this.extras = extras;
        }
        public int getOrder_count() {return order_count;}
        public String getMenu_name() {
            return menu_name;
        }

        public int getMenu_defaultprice() {
            return menu_defaultprice;
        }

        public ArrayList<HistoryDetailParsingHelper.extras> getExtras() {
            return extras;
        }

        public class extras {
            int extra_price;
            String extra_name;
            int extra_count;

            public extras(int extra_price, String extra_name, int extra_count) {
                this.extra_price = extra_price;
                this.extra_name = extra_name;
                this.extra_count = extra_count;
            }

            public int getExtra_price() {
                return extra_price;
            }

            public String getExtra_name() {
                return extra_name;
            }

            public int getExtra_count() {
                return extra_count;
            }
        }
    }
}
