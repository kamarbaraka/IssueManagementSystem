package com.kamar.issuemanagementsystem.ticket.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.jdbc.Work;

import java.io.Serializable;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ticket number generator.
 * @author kamar baraka.*/

public class TicketNumberGenerator extends SequenceStyleGenerator {

    private final SecureRandom random = new SecureRandom();

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        /*generate*/
//        return "T"+ super.generate(session, object)+ random.nextLong(100, 100000)+ "ims";
        final String[] result = new String[1];

        session.doWork(new Work() {

            @Override
            public void execute(Connection conn) throws SQLException {
                String prefix = "T";
                String suffix = "ims";

                PreparedStatement ps = conn.prepareStatement("UPDATE sequences SET sequence = " +
                        "LAST_INSERT_ID(sequence + 1) WHERE seq_name = ?");

                ps.setString(1, prefix + suffix);
                ps.executeUpdate();

                PreparedStatement ps2 = conn.prepareStatement("SELECT LAST_INSERT_ID()");
                ResultSet rs = ps2.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    result[0] = prefix + id+ random.nextInt(100, 1_000_000) + suffix;
                }
            }
        });

        if (result[0] == null) {
            throw new HibernateException("Could not generate ID");
        }

        return result[0];
    }

}
