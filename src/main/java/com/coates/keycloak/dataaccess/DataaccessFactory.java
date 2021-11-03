
package com.coates.keycloak.dataaccess;

import com.coates.keycloak.dataaccess.impl.MysqlDataaccess;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DataaccessFactory {

	public static final Dataaccess createDataaccess() {
		return new MysqlDataaccess();
	}
}
