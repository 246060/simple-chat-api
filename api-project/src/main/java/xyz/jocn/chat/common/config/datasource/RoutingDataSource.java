package xyz.jocn.chat.common.config.datasource;

import static xyz.jocn.chat.common.config.datasource.AppDataSourceProperties.*;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

	private CircularList<String> dataSourceNameList;

	@Override
	public void setTargetDataSources(Map<Object, Object> targetDataSources) {
		super.setTargetDataSources(targetDataSources);

		log.info("target datasource : {}", targetDataSources);
		dataSourceNameList = new CircularList<>(
			targetDataSources.keySet()
				.stream()
				.map(Object::toString)
				.filter(string -> string.contains(DATASOURCE_REPLICA_NAME))
				.collect(Collectors.toList())
		);
	}

	/**
	 * 현재 요청에서 사용할 DataSource 결정할 key값 반환
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
		if (isReadOnly) {
			log.debug("Connection Replica");
			return dataSourceNameList.getOne();
		} else {
			log.debug("Connection Primary");
			return DATASOURCE_PRIMARY_NAME;
		}
	}
}
