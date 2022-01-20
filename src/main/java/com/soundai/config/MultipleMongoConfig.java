package com.soundai.config;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoActionOperation;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.WriteConcernResolver;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MultipleMongoProperties.class)
public class MultipleMongoConfig {

	private final MultipleMongoProperties mongoProperties;

	@Primary
	@Bean(name = "primaryMongoTemplate")
	public MongoTemplate primaryMongoTemplate() throws Exception {
		MongoTemplate template = new MongoTemplate(primaryFactory(this.mongoProperties.getPrimary()));
//		template.setReadPreference(ReadPreference.primary());
		template.setWriteConcernResolver(natWriteConcernResolver());
		return template;
	}

	@Bean(name = "secondaryMongoTemplate")
	public MongoTemplate secondaryMongoTemplate() throws Exception {
		MongoTemplate template = new MongoTemplate(secondaryFactory(this.mongoProperties.getSecondary()));
//		template.setReadPreference(ReadPreference.secondary());
		return template;
	}

	@Bean
	@Primary
	public MongoDatabaseFactory primaryFactory(final MongoProperties mongo) throws Exception {
		return new SimpleMongoClientDatabaseFactory(mongo.getUri());
	}

	@Bean
	public MongoDatabaseFactory secondaryFactory(final MongoProperties mongo) throws Exception {
		return new SimpleMongoClientDatabaseFactory(mongo.getUri());
	}

	private WriteConcernResolver natWriteConcernResolver() {
		return action -> {
			if (action.getCollectionName().equals("customer")
					&& (action.getMongoActionOperation() == MongoActionOperation.INSERT
							|| action.getMongoActionOperation() == MongoActionOperation.SAVE
							|| action.getMongoActionOperation() == MongoActionOperation.UPDATE)) {
				return WriteConcern.MAJORITY;
			}
			return action.getDefaultWriteConcern();
		};
	}
}
