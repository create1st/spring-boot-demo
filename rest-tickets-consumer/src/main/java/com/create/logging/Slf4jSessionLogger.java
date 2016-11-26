/*
 * Copyright  2016 Sebastian Gil.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.create.logging;

import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Slf4jSessionLogger extends AbstractSessionLog {
    private static final String LOGGER_NAMESPACE = "eclipselink.logging.level";
    private static final String DEFAULT_CATEGORY = "default";

    private final Map<String, Logger> categoryLoggers;

    public Slf4jSessionLogger() {
        categoryLoggers = getCategoryLoggers();
    }

    private Map<String, Logger> getCategoryLoggers() {
        return Stream.concat(
                Arrays.stream(SessionLog.loggerCatagories),
                Stream.of(DEFAULT_CATEGORY)
        )
                .collect(Collectors.toMap(Function.identity(), this::createLogger));
    }

    private Logger createLogger(String category) {
        return LoggerFactory.getLogger(getLoggerName(category));
    }

    private String getLoggerName(String category) {
        return String.format("%s.%s", LOGGER_NAMESPACE, category);
    }

    @Override
    public void log(SessionLogEntry entry) {
        if (shouldLog(entry.getLevel(), entry.getNameSpace())) {
            final Logger logger = getLogger(entry.getNameSpace());
            final int logLevel = entry.getLevel();
            final String message = getSupplementDetailString(entry) + formatMessage(entry);
            log(logger, logLevel, message);
        }
    }

    private void log(Logger logger,
                     int logLevel,
                     String message) {
        switch (logLevel) {
            case SessionLog.ALL:
            case SessionLog.FINEST:
            case SessionLog.FINER:
                logger.trace(message);
                break;
            case SessionLog.FINE:
            case SessionLog.CONFIG:
                logger.debug(message);
                break;
            case SessionLog.INFO:
                logger.info(message);
                break;
            case SessionLog.WARNING:
                logger.warn(message);
                break;
            case SessionLog.SEVERE:
                logger.error(message);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean shouldLog(int level, String category) {
        final Logger logger = getLogger(category);
        switch (level) {
            case SessionLog.ALL:
            case SessionLog.FINEST:
            case SessionLog.FINER:
                return logger.isTraceEnabled();
            case SessionLog.FINE:
            case SessionLog.CONFIG:
                return logger.isDebugEnabled();
            case SessionLog.INFO:
                return logger.isInfoEnabled();
            case SessionLog.WARNING:
                return logger.isInfoEnabled();
            case SessionLog.SEVERE:
                return logger.isInfoEnabled();
            default:
                return false;
        }
    }

    private Logger getLogger(String category) {
        return categoryLoggers.getOrDefault(category, getDefaultLogger());
    }

    private Logger getDefaultLogger() {
        return categoryLoggers.get(DEFAULT_CATEGORY);
    }

    @Override
    public boolean shouldLog(int level) {
        return shouldLog(level, DEFAULT_CATEGORY);
    }

    @Override
    public boolean shouldDisplayData() {
        return shouldDisplayData != null && shouldDisplayData;
    }
}