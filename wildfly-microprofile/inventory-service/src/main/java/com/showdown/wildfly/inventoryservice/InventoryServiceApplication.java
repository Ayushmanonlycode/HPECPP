package com.showdown.wildfly.inventoryservice;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;
import jakarta.annotation.security.DeclareRoles;

@LoginConfig(authMethod = "MP-JWT")
@DeclareRoles({"ADMIN", "CUSTOMER"})
@ApplicationPath("/")
public class InventoryServiceApplication extends Application {
}