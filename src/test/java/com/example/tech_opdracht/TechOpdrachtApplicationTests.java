package com.example.tech_opdracht;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TechOpdrachtApplicationTests {
	@LocalServerPort
	private int port;

	final OkHttpClient client = new OkHttpClient.Builder()
			.connectTimeout(30, TimeUnit.SECONDS)
			.readTimeout(30, TimeUnit.SECONDS)
			.build();

	final ObjectMapper objectMapper = new ObjectMapper()
			.registerModule(new JavaTimeModule())
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	@Test
	void FirstPostShouldAlwaysReturn444() {
		final String json = """
                {
                    "id": 42,
                    "name": "Ada lovelace",
                    "birthDate": "1814-12-10",
                    "parent1": { "id": 1 },
                    "parent2": { "id": 2 },
                    "partner": { "id": 3 },
                    "children": [{"id": 77 }, { "id": 78 }]
                }
                """;

		final var request = createRequest(json);

		try (Response response = client.newCall(request).execute()) {
			assertEquals(444, response.code());
		} catch (IOException e) {
            fail(e.getMessage());
        }
    }

	@Test
	void CorrectCase() {
		final String parent1 = """
                {
                    "id": 42,
                    "name": "Ada lovelace",
                    "birthDate": "1814-12-10",
                    "parent1": { "id": 1 },
                    "parent2": { "id": 2 },
                    "partner": { "id": 3 },
                    "children": [{"id": 77 }, { "id": 78 }, {"id": 79}]
                }
                """;
		final String parent2 = """
                {
                    "id": 3,
                    "name": "Beta lovelace",
                    "birthDate": "1814-12-10",
                    "parent1": { "id": 123 },
                    "parent2": { "id": 124 },
                    "partner": { "id": 42 },
                    "children": [{"id": 77 }, { "id": 78 }, {"id": 79}]
                }
                """;
		final String child1 = """
                {
                    "id": 78,
                    "name": "child1",
                    "birthDate": "2025-01-01",
                    "parent1": { "id": 42 },
                    "parent2": { "id": 3 },
                    "partner":  null,
                    "children": []
                }
                """;
		final String child2 = """
                {
                    "id": 79,
                    "name": "child2",
                    "birthDate": "2025-01-01",
                    "parent1": { "id": 42 },
                    "parent2": { "id": 3 },
                    "partner":  null,
                    "children": []
                }
                """;

		final String youngestChild = """
                {
                    "id": 77,
                    "name": "youngest",
                    "birthDate": "2025-01-01",
                    "parent1": { "id": 42 },
                    "parent2": { "id": 3 },
                    "partner":  null,
                    "children": []
                }
                """;

		final List<String> family = List.of(parent1, parent2, child2, child1);

		for (final var person : family) {
			final Request request = createRequest(person);

			try (Response _ = client.newCall(request).execute()) {
			} catch (IOException e) {
				fail(e.getMessage());
			}
		}

		final Request requestYoungestChild = createRequest(youngestChild);

		try (Response response = client.newCall(requestYoungestChild).execute()) {
			assertEquals(200, response.code(), "should now adhere to all criteria");
			final String responseBody = response.body().string();
			final var people = objectMapper.readValue(responseBody, new TypeReference<List<PersonDTO>>() {});

			final var ids = people.stream().map(PersonDTO::id).collect(Collectors.toSet());
			assertEquals(Set.of(3, 42), ids);

		} catch (Exception e) {
            fail(e.getMessage());
		}
	}

	@NotNull
	private Request createRequest(String parent1) {
		RequestBody body = RequestBody.create(parent1, MediaType.get("application/json"));

		return new Request.Builder()
				.url("http://localhost:" + port  + "/api/v1/people")
				.post(body)
				.addHeader("Content-Type", "application/json")
				.build();
	}
}
