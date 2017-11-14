package lk.ac.mrt.cse.medipal.model;

public class DrugRoute {
    private int route_id;
    private String route_name;

    public DrugRoute(int route_id, String route_name) {
        this.route_id = route_id;
        this.route_name = route_name;
    }

    public DrugRoute(){}

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }


}
