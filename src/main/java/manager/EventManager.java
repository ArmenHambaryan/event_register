package manager;


import db.DBConnectionProvider;
import model.Event;
import model.EventType;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class EventManager {

    private Connection connection;


    public EventManager() {
        connection = DBConnectionProvider.getInstance().getConnection();
    }

    public void addEvent(Event event) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT into event (name,place,isOnline,price,event_type) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, event.getName());
        preparedStatement.setString(2, event.getPlace());
        preparedStatement.setBoolean(3, event.isOnline());
        preparedStatement.setDouble(4, event.getPrice());
        preparedStatement.setString(5, event.getEventType().name());

        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            int anInt = generatedKeys.getInt(1);
            event.setId(anInt);
        }

    }

    public List<Event> showEvent() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * from event");

        List<Event> events = new LinkedList<>();

        if (resultSet.next()) {
            Event event = new Event();

            event.setName(resultSet.getString("name"));
            event.setPlace(resultSet.getString("place"));
            event.setOnline(resultSet.getBoolean("isOnline"));
            event.setId(resultSet.getInt("id"));
            event.setEventType(EventType.valueOf(resultSet.getString("event_type")));
            event.setPrice(resultSet.getDouble("price"));

            events.add(event);
        }
        return events;
    }


}
