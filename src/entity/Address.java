package entity;

public class Address {

    private District district;
    private String description;

    public Address(District district, String description) {
        setDistrict(district);
        setDescription(description);
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        if (district == null) {
            throw new IllegalArgumentException("WRONG: district can not be null ");
        }
        this.district = district;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("WRONG: description can not be empty");
        this.description = description;
    }

    @Override
    public String toString() {
        return "\nAddress" +
                "   \ndistrict: " + district +
                "   \ndescription: " + description + '\n';
    }
}
