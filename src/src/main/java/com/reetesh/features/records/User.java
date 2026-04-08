package com.reetesh.features.records;
/*
In Java, a record is a special kind of class introduced in Java 14 (standardised in Java 16) that acts as a transparent,
immutable carrier for data. It significantly reduces the "boilerplate" code—like constructors, getters, equals(), hashCode(),
and toString()—traditionally required for data-transfer objects (DTOs)

1. Basic Syntax: A record is defined with the record keyword. A single line can replace an entire class of 50+ lines
    This single line automatically generates:
    Private final fields for name and age.
    A Canonical Constructor that initializes all fields.
    Public Accessor Methods named name() and age() (no get prefix).
    equals() and hashCode() based on the state of the components.
    A toString() method that prints the record name and its values (e.g., User[name=Alice, age=30]).

2. Key Characteristics
    Immutability: Records are designed to be shallowly immutable. Once created, their fields cannot be changed.
    Implicitly Final: You cannot extend a record, and a record cannot extend any other class (it implicitly extends java.lang.Record).
    No Instance Fields: You cannot declare additional instance variables in the body; only static fields are allowed.
    Compact Constructors: You can add validation logic without repeating the parameter list:

3. When to Use Records
    DTOs and API Responses: Perfect for carrying data between layers or representing JSON payloads.
    Local Data Structures: Use them inside methods to group temporary intermediate results.
    Value Objects: When you care about the value of the data rather than its identity.

4. Records vs. Normal Classes
    Feature 	Normal Class	Record
    Boilerplate	High (must write getters, etc.)	Low (one-liner)
    Mutators	Setters allowed	No setters (immutable)
    Inheritance	Can extend other classes	Cannot extend any class
    Field Access	getName()	name()

*/
public record User(String name, int age) {
    public User{
        if(age<0) throw new IllegalArgumentException("Age can not be negative");
    }
}
