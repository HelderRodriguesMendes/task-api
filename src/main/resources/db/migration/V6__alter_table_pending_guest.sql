ALTER TABLE `cursoKeycloakdb`.pending_guest ADD keycloak_id varchar(100) NULL;
CREATE INDEX pending_guest_guest_email_IDX USING BTREE ON `cursoKeycloakdb`.pending_guest (guest_email);