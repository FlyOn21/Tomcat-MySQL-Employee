package org.example.app.db_connect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.app.repository.EmployeeRepository;

import java.util.List;

public class GetConnect {
    private static final Logger DB_INIT_CONTROLLER_LOGGER =
            LogManager.getLogger(EmployeeRepository.class);
    private static final Logger CONSOLE_LOGGER =
            LogManager.getLogger("console_logger");
    public DbConnectInit connect() {
        DbConnectInit connectInit = new DbConnectInit();
        if(connectInit.isConnected()) {
            return connectInit;
        }
        else {
            List<String> errors = connectInit.getConnectErrors();
            StringBuilder errorString = new StringBuilder();
            for (String error : errors) {
                errorString.append(error).append("\n");
            }
            DB_INIT_CONTROLLER_LOGGER.error(errorString.toString());
            CONSOLE_LOGGER.error(errorString.toString());
            throw new RuntimeException(errorString.toString());
        }
    }
}
