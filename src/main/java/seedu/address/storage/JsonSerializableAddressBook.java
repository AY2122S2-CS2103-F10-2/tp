package seedu.address.storage;

import static seedu.address.logic.commands.AddDishCommand.MESSAGE_DUPLICATE_DISH;
import static seedu.address.logic.commands.AddDriverCommand.MESSAGE_DUPLICATE_DRIVER;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.driver.Driver;
import seedu.address.model.item.Dish;
import seedu.address.model.item.Person;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedDish> dishes = new ArrayList<>();
    private final List<JsonAdaptedDriver> drivers = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
            @JsonProperty("dishes") List<JsonAdaptedDish> dishes,
            @JsonProperty("drivers") List<JsonAdaptedDriver> drivers) {
        this.persons.addAll(persons);
        this.dishes.addAll(dishes);
        this.drivers.addAll(drivers);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
        dishes.addAll(source.getDishList().stream().map(JsonAdaptedDish::new).collect(Collectors.toList()));
        drivers.addAll(source.getDriverList().stream().map(JsonAdaptedDriver::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (addressBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addPerson(person);
        }

        for (JsonAdaptedDish jsonAdaptedDish : dishes) {
            Dish dish = jsonAdaptedDish.toModelType();
            if (addressBook.hasDish(dish)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_DISH);
            }
            addressBook.addDish(dish);
        }

        for (JsonAdaptedDriver jsonAdaptedDriver : drivers) {
            Driver driver = jsonAdaptedDriver.toModelType();
            if (addressBook.hasDriver(driver)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_DRIVER);
            }
            addressBook.addDriver(driver);
        }
        return addressBook;
    }

}
