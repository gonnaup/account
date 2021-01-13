package org.gonnaup.accountmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author gonnaup
 * @version 2021/1/13 11:06
 */
@Data
@AllArgsConstructor(staticName = "of")
public class SimpleBooleanShell {
    private boolean flag;
}
