package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.AdminDao;
import net.thumbtack.onlineshop.dao.ClientDao;
import net.thumbtack.onlineshop.dao.SessionDao;
import org.mockito.Mock;

public class BaseServiceTest {
    @Mock
    protected SessionDao sessionDao;

    @Mock
    protected AdminDao adminDao;

    @Mock
    protected ClientDao clientDao;
}