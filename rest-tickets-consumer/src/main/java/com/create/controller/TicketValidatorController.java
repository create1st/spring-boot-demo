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

package com.create.controller;

import com.create.model.Ticket;
import com.create.validation.ValidationResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.create.controller.ResponseMessage.BAD_REQUEST_RESPONSE;
import static com.create.controller.ResponseMessage.FAILURE_RESPONSE;
import static com.create.controller.ResponseMessage.FORBIDDEN_RESPONSE;
import static com.create.controller.ResponseMessage.NOT_FOUND_RESPONSE;
import static com.create.controller.ResponseMessage.SUCCESS_RESPONSE;
import static com.create.controller.ResponseMessage.UNAUTHORIZED_RESPONSE;
import static com.create.security.AccessControl.HAS_AUTHORITY_BATCH_WRITER;
import static com.create.security.AccessControl.HAS_ROLE_WRITER;

@RestController
@RequestMapping("${validator.context-path:}")
@Api(description = "Endpoint for ticket validation")
public class TicketValidatorController {

    @ApiOperation(value = "Validate ticket", notes = "Validate ticket using this REST API")
    @PostMapping("tickets")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS_RESPONSE),
            @ApiResponse(code = 400, message = BAD_REQUEST_RESPONSE, response = ValidationResult.class),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE),
            @ApiResponse(code = 500, message = FAILURE_RESPONSE)})
    @ResponseBody
    @PreAuthorize(HAS_ROLE_WRITER)
    public void validateTicket(@Valid @RequestBody Ticket ticket,
                               @AuthenticationPrincipal User user) {
    }

    @ApiOperation(value = "Validate tickets in batch", notes = "Validate tickets in batch using this REST API")
    @PostMapping("tickets/batch")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS_RESPONSE),
            @ApiResponse(code = 400, message = BAD_REQUEST_RESPONSE, response = ValidationResult.class),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE),
            @ApiResponse(code = 500, message = FAILURE_RESPONSE)})
    @ResponseBody
    @PreAuthorize(HAS_AUTHORITY_BATCH_WRITER)
    public void validateTickets(@Valid @RequestBody List<Ticket> tickets,
                                @AuthenticationPrincipal User user) {
    }
}
