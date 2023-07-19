-- Create locations table
CREATE TABLE locations
(
    id            UUID PRIMARY KEY,
    country       VARCHAR(255) NOT NULL,
    city          VARCHAR(255) NOT NULL,
    date_created  TIMESTAMP,
    date_modified TIMESTAMP
);

-- Create advisors table
CREATE TABLE advisors
(
    user_id       UUID PRIMARY KEY REFERENCES users (id),
    date_of_birth DATE,
    location_id   UUID REFERENCES locations (id),
    CONSTRAINT fk_advisors_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Create advisees table
CREATE TABLE advisees
(
    user_id       UUID PRIMARY KEY REFERENCES users (id),
    date_of_birth DATE,
    location_id   UUID REFERENCES locations (id),
    advisor_id    UUID REFERENCES advisors (user_id),
    CONSTRAINT fk_advisees_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_advisees_advisor_id FOREIGN KEY (advisor_id) REFERENCES advisors (user_id)
);
