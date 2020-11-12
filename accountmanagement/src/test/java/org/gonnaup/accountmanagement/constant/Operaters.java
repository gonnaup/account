package org.gonnaup.accountmanagement.constant;

import org.gonnaup.accountmanagement.domain.Operater;
import org.gonnaup.accountmanagement.enums.OperaterType;

/**
 * @author gonnaup
 * @version 1.0
 * @Created on 2020/11/12 8:53
 */
public abstract class Operaters {
    public static final Operater ADMIN = Operater.of(OperaterType.A, 1000000000L, "admin");
}
