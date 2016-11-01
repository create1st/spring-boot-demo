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

package com.create.model;

import com.create.databind.MoneyDeserializer;
import com.create.databind.MoneySerializer;
import com.create.validation.AfterDate;
import com.create.validation.Choice;
import com.create.validation.Currency;
import com.create.validation.MultiDateConstraint;
import com.create.validation.MultiDateConstraints;
import com.create.validation.NotBeforeDate;
import com.create.validation.NotWeekend;
import com.create.validation.RequiredOnPropertyValue;
import com.create.validation.RequiredOnPropertyValues;
import com.create.validation.SupportedSourceSystem;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.NONE)
@MultiDateConstraints({
        @MultiDateConstraint(baseDateProperty = "expectedDate", termDateProperty = "ticketDate",
                constraintChecker = NotBeforeDate.class,
                message = "{expectedDateNotBeforeTicketDate.message}"),
        @MultiDateConstraint(baseDateProperty = "expiryDate", termDateProperty = "ticketDate",
                constraintChecker = AfterDate.class,
                message = "{expiryDateAfterTicketDate.message}"),
})
@RequiredOnPropertyValues({
        @RequiredOnPropertyValue(property = "ticketType", values = {"EXTERNAL"}, requiredProperty = "externalSource")
})
public class Ticket implements Serializable {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_FORMAT_EXAMPLE = "2016-01-01";
    private static final String SOURCE_SYSTEM_EXAMPLE = "SYSTEM_03";
    private static final String PRICE_EXAMPLE = "10.00";
    private static final String CCY_EXAMPLE = "USD";
    private static final String TICKET_TYPE_EXAMPLE = "EXTERNAL";
    private static final String EXTERNAL_SOURCE_EXAMPLE = "External source";

    @ApiModelProperty(example = SOURCE_SYSTEM_EXAMPLE)
    @NotNull
    @SupportedSourceSystem
    private String sourceSystem;
    @ApiModelProperty(example = DATE_FORMAT_EXAMPLE)
    @JsonFormat(pattern = DATE_FORMAT)
    @NotNull
    private LocalDate ticketDate;
    @ApiModelProperty(example = DATE_FORMAT_EXAMPLE)
    @JsonFormat(pattern = DATE_FORMAT)
    @NotWeekend
    private LocalDate expectedDate;
    @ApiModelProperty(example = DATE_FORMAT_EXAMPLE)
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDate expiryDate;
    @ApiModelProperty(example = PRICE_EXAMPLE)
    @NotNull
    @JsonDeserialize(using = MoneyDeserializer.class)
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal price;
    @ApiModelProperty(example = CCY_EXAMPLE)
    @Currency
    @NotNull
    private String currency;
    @ApiModelProperty(example = TICKET_TYPE_EXAMPLE)
    @Choice(valueSource = TicketType.class, message = "{ticketTypeNotValid.message}")
    private String ticketType;
    @ApiModelProperty(example = EXTERNAL_SOURCE_EXAMPLE)
    private String externalSource;

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public LocalDate getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(LocalDate ticketDate) {
        this.ticketDate = ticketDate;
    }

    public LocalDate getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(LocalDate expectedDate) {
        this.expectedDate = expectedDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getExternalSource() {
        return externalSource;
    }

    public void setExternalSource(String externalSource) {
        this.externalSource = externalSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(sourceSystem, ticket.sourceSystem) &&
                Objects.equals(ticketDate, ticket.ticketDate) &&
                Objects.equals(expectedDate, ticket.expectedDate) &&
                Objects.equals(expiryDate, ticket.expiryDate) &&
                Objects.equals(price, ticket.price) &&
                Objects.equals(currency, ticket.currency) &&
                Objects.equals(ticketType, ticket.ticketType) &&
                Objects.equals(externalSource, ticket.externalSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceSystem, ticketDate, expectedDate, expiryDate, price, currency, ticketType, externalSource);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "sourceSystem='" + sourceSystem + '\'' +
                ", ticketDate=" + ticketDate +
                ", expectedDate=" + expectedDate +
                ", expiryDate=" + expiryDate +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", ticketType='" + ticketType + '\'' +
                ", externalSource='" + externalSource + '\'' +
                '}';
    }
}
