package com.armin.infrastructure.config.init;


import com.armin.utility.object.SystemException;

import java.io.IOException;

/**
 * @author : Armin.Nik
 * @project : map-helm
 * @date : 09.10.22
 */
public interface IPropertyInitializer {
    Object initialize() throws IOException, SystemException;

    void updateProperties(Object model) throws IOException, SystemException;

    Object moveToNextRound() throws IOException, SystemException;
}
