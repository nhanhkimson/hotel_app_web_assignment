# Hotel Management System

A comprehensive hotel management system built with Spring Boot backend and modern JavaScript frontend using Tailwind CSS.

## Features

### Backend (Spring Boot)
- **Full CRUD Operations** for all entities:
  - Hotels
  - Rooms
  - Room Types
  - Bookings
  - Bills
  - Guests
  - Employees
  - Roles

- **RESTful API** with pagination support
- **PostgreSQL Database** integration
- **JWT Authentication** ready
- **Exception Handling** with custom exceptions

### Frontend (JavaScript + Tailwind CSS)
- **Modern UI/UX** with smooth animations
- **Responsive Design** - works on all devices
- **Real-time Search** and filtering
- **Enhanced Components**:
  - Toast notifications
  - Loading spinners
  - Modal dialogs
  - Form validation
  - Pagination

- **Dashboard** with statistics and recent activity
- **Beautiful Tables** with hover effects
- **Smooth Transitions** and animations

## Technology Stack

- **Backend**: Spring Boot, Spring Data JPA, PostgreSQL
- **Frontend**: Vanilla JavaScript, Tailwind CSS
- **Build Tool**: Gradle

## Project Structure

```
hotel_mangement/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/example/hotel_mangement/
│   │   │       ├── controller/        # REST & Web controllers
│   │   │       ├── service/          # Service interfaces & implementations
│   │   │       ├── repository/       # JPA repositories
│   │   │       ├── model/            # Entities, DTOs, Requests, Responses
│   │   │       └── exception/       # Custom exceptions
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── js/              # JavaScript modules
│   │       │   │   ├── api.js       # API service
│   │       │   │   ├── app.js       # Main app logic
│   │       │   │   ├── pages.js     # Page handlers
│   │       │   │   └── utils.js     # Utilities & components
│   │       │   └── css/
│   │       │       └── custom.css   # Custom styles
│   │       └── templates/           # HTML templates
│   └── test/
└── build.gradle
```

## Getting Started

### Prerequisites
- Java 17 or higher
- PostgreSQL database
- Gradle (or use Gradle wrapper)

### Database Setup
1. Create a PostgreSQL database named `hotel_sys`
2. Update `application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5431/hotel_sys
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### Running the Application
1. Clone the repository
2. Navigate to the project directory
3. Run the application:
```bash
./gradlew bootRun
```
Or use your IDE to run `HotelManagementApplication`

4. Open your browser and navigate to:
```
http://localhost:8080
```

## API Endpoints

### Hotels
- `GET /api/v1/hotel` - Get all hotels (paginated)
- `GET /api/v1/hotel/{id}` - Get hotel by ID
- `POST /api/v1/hotel` - Create hotel
- `PUT /api/v1/hotel/{id}` - Update hotel
- `DELETE /api/v1/hotel/{id}` - Delete hotel

### Rooms
- `GET /api/v1/room` - Get all rooms (paginated)
- `GET /api/v1/room/{id}` - Get room by ID
- `POST /api/v1/room` - Create room
- `PUT /api/v1/room/{id}` - Update room
- `DELETE /api/v1/room/{id}` - Delete room

### Room Types
- `GET /api/v1/room-type` - Get all room types (paginated)
- `GET /api/v1/room-type/{id}` - Get room type by ID
- `POST /api/v1/room-type` - Create room type
- `PUT /api/v1/room-type/{id}` - Update room type
- `DELETE /api/v1/room-type/{id}` - Delete room type

### Bookings
- `GET /api/v1/booking` - Get all bookings (paginated)
- `GET /api/v1/booking/{id}` - Get booking by ID
- `POST /api/v1/booking` - Create booking
- `PUT /api/v1/booking/{id}` - Update booking
- `DELETE /api/v1/booking/{id}` - Delete booking

### Bills
- `GET /api/v1/bill` - Get all bills (paginated)
- `GET /api/v1/bill/{id}` - Get bill by ID
- `POST /api/v1/bill` - Create bill
- `PUT /api/v1/bill/{id}` - Update bill
- `DELETE /api/v1/bill/{id}` - Delete bill

### Guests
- `GET /api/v1/guest` - Get all guests
- `GET /api/v1/guest/{id}` - Get guest by ID

### Employees
- `GET /api/v1/employee` - Get all employees

### Roles
- `GET /api/v1/role` - Get all roles (paginated)
- `GET /api/v1/role/{id}` - Get role by ID
- `POST /api/v1/role` - Create role
- `PUT /api/v1/role/{id}` - Update role
- `DELETE /api/v1/role/{id}` - Delete role

## Features in Detail

### UI Components

#### Toast Notifications
- Success, error, warning, and info types
- Auto-dismiss with smooth animations
- Manual dismiss option

#### Loading Spinner
- Global loading state management
- Automatic show/hide on API calls

#### Modal Dialogs
- Beautiful gradient headers
- Smooth animations
- ESC key and backdrop click to close
- Form support

#### Form Validation
- Real-time validation
- Visual feedback
- Error messages

#### Search & Filter
- Real-time table filtering
- Debounced search input
- Multi-column search support

## Code Quality

- **Clean Architecture** - Separation of concerns
- **Modular JavaScript** - Reusable components
- **Error Handling** - Comprehensive error management
- **Responsive Design** - Mobile-first approach
- **Accessibility** - Keyboard navigation support

## Future Enhancements

- [ ] User authentication and authorization
- [ ] Advanced reporting and analytics
- [ ] Email notifications
- [ ] File upload for room images
- [ ] Calendar view for bookings
- [ ] Export functionality (PDF, Excel)
- [ ] Multi-language support
- [ ] Dark mode theme

## License

This project is for educational purposes.

## Author

Hotel Management System - Complete Full-Stack Application



