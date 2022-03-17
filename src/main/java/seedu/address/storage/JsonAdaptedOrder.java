package seedu.address.storage;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.customer.AddressCustomer;
import seedu.address.model.customer.Customer;
import seedu.address.model.customer.NameCustomer;
import seedu.address.model.customer.PhoneCustomer;
import seedu.address.model.driver.Driver;
import seedu.address.model.driver.NameDriver;
import seedu.address.model.driver.PhoneDriver;
import seedu.address.model.item.Dish;
import seedu.address.model.item.Name;
import seedu.address.model.order.Order;

/**
 * Jackson-friendly version of {@link Order}.
 */
class JsonAdaptedOrder {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Order's %s field is missing!";

    private final String customerName;
    private final String customerPhone;
    private final String customerAddress;
    private final String driverName;
    private final String driverPhone;
    private final ArrayList<String> dishes;

    /**
     * Constructs a {@code JsonAdaptedOrder} with the given dish details.
     */
    @JsonCreator
    public JsonAdaptedOrder(@JsonProperty("customerName") String customerName,
                            @JsonProperty("customerPhone") String customerPhone,
                            @JsonProperty("customerAddress") String customerAddress,
                            @JsonProperty("driverName") String driverName,
                            @JsonProperty("driverPhone") String driverPhone,
                            @JsonProperty("dishes") ArrayList<String> dishes) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.dishes = dishes;
    }

    /**
     * Converts a given {@code Order} into this class for Jackson use.
     */
    public JsonAdaptedOrder(Order source) {
        customerName = source.getCustomerName();
        customerPhone = source.getCustomerPhone();
        customerAddress = source.getCustomerAddress().toString();
        driverName = source.getDriverName();
        driverPhone = source.getDriver().getPhone().toString();
        dishes = new ArrayList<String>();
        for (Dish dish : source.getDishes()) {
            dishes.add(dish.toString());
        }
    }

    /**
     * Converts this Jackson-friendly adapted order object into the model's {@code Order} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted order.
     */
    public Order toModelType() throws IllegalValueException {
        if (customerName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, NameCustomer.class.getSimpleName()));
        }

        if (customerPhone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, PhoneCustomer.class.getSimpleName()));
        }

        if (customerAddress == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, AddressCustomer.class.getSimpleName()));
        }

        final Customer customer = new Customer(new NameCustomer(customerName), new PhoneCustomer(customerPhone), new AddressCustomer(customerAddress));

        if (driverName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, NameDriver.class.getSimpleName()));
        }

        if (driverPhone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, PhoneDriver.class.getSimpleName()));
        }

        final Driver driver = new Driver(new NameDriver(driverName), new PhoneDriver(driverPhone));

        if (dishes == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }

        final ArrayList<Dish> modelDishes = new ArrayList<Dish> ();
        for (String dish : dishes) {
            modelDishes.add(new Dish(new Name(dish)));
        }

        return new Order(customer, driver, modelDishes.toArray(new Dish[0]));
    }

}

