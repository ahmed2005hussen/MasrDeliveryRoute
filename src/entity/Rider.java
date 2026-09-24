package entity;

import service.GenerateIdService;

public class Rider {

    private GenerateIdService generateIdService = new GenerateIdService();
    private final String riderId;
    private String riderName;
    private VehicleType vehicleType;
    private Address address;
    private boolean isAvailable;
    private int CompletedDeliveries;
    private Order activeOrder;

    public Rider(String riderName, VehicleType vehicleType,
                  Address address, boolean isAvailable, int completedDeliveries) {
        this.riderId = generateIdService.generateId(riderName);
        this.riderName = riderName;
        this.vehicleType = vehicleType;
        this.address = address;
        this.isAvailable = isAvailable;
        CompletedDeliveries = completedDeliveries;
    }

    public String getRiderId() {
        return riderId;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getCompletedDeliveries() {
        return CompletedDeliveries;
    }

    public void assignOrder(Order order) {
        if (!isAvailable) {
            throw new IllegalStateException("WRONG: Rider is not available");
        }
        this.isAvailable = false;
        this.activeOrder = order;
    }

    public void completeDelivery() {

        if (activeOrder == null) {
            throw new IllegalStateException("WRONG: Rider has no Order");
        }

        activeOrder = null;
        isAvailable = true;
        CompletedDeliveries++;
    }

    public Order getActiveOrder() {
        if (activeOrder == null) {
            throw new NullPointerException("Rider has no Orders");
        }
        return activeOrder;
    }
}
