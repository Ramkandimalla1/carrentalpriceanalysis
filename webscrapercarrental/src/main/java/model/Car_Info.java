package model;

// model or entity for storing data
public class Car_Info {
    private String name;
    private double price;
    private int passenger_Capacity;
    private String car_Group;
    private String transmission_Type;
    private int large_Bag;
    private int small_Bag;
    private String rental_Company;

    public Car_Info(String name, double price, int passengerCapacity, String carGroup, String transmissionType, int largeBag, int smallBag, String rentalCompany) {
        this.name = name;
        this.price = price;
        this.passenger_Capacity = passengerCapacity;
        this.car_Group = carGroup;
        this.transmission_Type = transmissionType;
        this.large_Bag = largeBag;
        this.small_Bag = smallBag;
        this.rental_Company = rentalCompany;
    }

    public String getRentalCompany() {
        return rental_Company;
    }

    public void setRentalCompany(String rentalCompany) {
        this.rental_Company = rentalCompany;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getPassengerCapacity() {
        return passenger_Capacity;
    }

    public String getTransmissionType() {
        return transmission_Type;
    }

    public String getCarGroup() {
        return car_Group;
    }

    public int getLargeBag() {
        return large_Bag;
    }

    public int getSmallBag() {
        return small_Bag;
    }

	@Override
	public String toString() {
		return "Car_Info [name=" + name + ", price=" + price + ", passenger_Capacity=" + passenger_Capacity
				+ ", car_Group=" + car_Group + ", transmission_Type=" + transmission_Type + ", large_Bag=" + large_Bag
				+ ", small_Bag=" + small_Bag + ", rental_Company=" + rental_Company + "]";
	}
    
}
