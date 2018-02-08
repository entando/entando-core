package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.entando.entando.aps.system.services.oauth2.model.ConsumerRecordVO;

import java.util.List;

public interface IOAuthConsumerDAO {

    ConsumerRecordVO getConsumer(String clientId);

    List<String> getConsumerKeys(FieldSearchFilter[] filters);

    void addConsumer(ConsumerRecordVO consumer);

    void updateConsumer(ConsumerRecordVO consumer);

    void deleteConsumer(String clientId);
}
