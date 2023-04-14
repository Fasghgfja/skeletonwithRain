package at.qe.skeleton.repositories;
import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.LogType;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * Repository for managing the {@link Log} Log entities.
 *  * The Derived Query are split into two parts separated by keywords:
 *  * The first one is the introducer(e.g find.., read.., query.., ...)
 *  * The second one defines the criteria (e.g ...ByName, ...).
 */
public interface LogRepository extends AbstractRepository<Log, Long> {
    long countLogByType(LogType logType);
    long count();


}
