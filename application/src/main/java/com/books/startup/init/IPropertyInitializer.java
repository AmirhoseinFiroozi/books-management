package com.books.startup.init;




import com.books.utility.system.exception.SystemException;

import java.io.IOException;

/**
 * @auther : Armin.Nik
 * @project : vrp-helm
 * @date : 09.10.22
 */
public interface IPropertyInitializer {
    Object initialize() throws SystemException;
}
