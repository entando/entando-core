/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.web.common.annotation;

import javax.validation.Payload;

/**
 *
 * @author paddeo
 */
public @interface ValidateString {

    String[] acceptedValues() default {};

    String message() default "{string.notValid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
