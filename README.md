# its4243-assignment02-ict21894

smart-travel-platform/
│
├── user-service/            (Port 8080)
├── flight-service/          (Port 8081)
├── hotel-service/           (Port 8082)
├── booking-service/         (Port 8083)   ← Orchestrator
├── payment-service/         (Port 8084)
└── notification-service/    (Port 8085)


Each service runs independently with its own controller, DTOs, services, entities, database tables, and error handling.

Service Responsibilities

1. User Service (8080)
Manages user accounts.
Create user
Update user
Delete user
View user details
Paginated user listing

2. Flight Service (8081)
Handles flight operations.
Create, update, delete flights
Check seat availability
Reserve seats
Get flight details
Paginated listing

3. Hotel Service (8082)
Manages hotels and room availability.
Create, update, delete hotels
Check room availability
Reserve hotel rooms
Paginated listing

4. Payment Service (8084)
Processes payments requested by the booking-service.

5. Notification Service (8085)
Sends simple text-based booking notifications.

6. Booking Service as the Orchestrator (8083)
The Booking Service coordinates all other microservices to complete a single booking request.


Booking Workflow 

When the client calls POST /api/bookings the booking-service executes this sequence

Validate User
Calls user-service → /api/users/{id}

Check Flight Availability
Calls flight-service (Feign) → /api/flights/{id}/availability

Check Hotel Availability
Calls hotel-service (Feign) → /api/hotels/{id}/availability

Reserve Flight Seats
Calls PUT /api/flights/{id}/reserve-seats

Reserve Hotel Rooms
Calls PUT /api/hotels/{id}/reserve

Process Payment
Calls payment-service → /api/payments

Send Notification
Calls notification-service → /api/notifications

Save Booking & Return Response
Returns booking details + status.


Communication Flow 
Client → Booking Service (Orchestrator)
          |
          |--→ User Service (validate)
          |--→ Flight Service (check + reserve)
          |--→ Hotel Service (check + reserve)
          |--→ Payment Service (charge)
          |--→ Notification Service (send message)
          |
          +--→ Booking saved and returned to client


How to Run the Project

Open the project structure in IntelliJ IDEA.
Open each microservice folder individually.
Run the following classes one by one

Service	Main Class	Port
user-service	UserServiceApplication	8080
flight-service	FlightServiceApplication	8081
hotel-service	HotelServiceApplication	8082
booking-service	BookingServiceApplication	8083
payment-service	PaymentServiceApplication	8084
notification-service	NotificationServiceApplication	8085

Testing With Postman

Import file
Smart Travel Platform ICT21894.postman_collection
