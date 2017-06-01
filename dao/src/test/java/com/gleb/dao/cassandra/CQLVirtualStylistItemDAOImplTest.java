package com.gleb.dao.cassandra;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gleb.dao.VirtualStylistItemDAO;
import com.gleb.dao.objects.DBVirtualStylistCanvasItem;

/**
 * Created by gleb on 31.10.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class CQLVirtualStylistItemDAOImplTest {

    @Autowired
    private VirtualStylistItemDAO itemDAO;

    @Autowired
    private BasicCQLCassandraDAO basicDAO;

    public void cleanUp() {
        basicDAO.getSession().execute("truncate " + CQLVirtualStylistItemDAOImpl.MATCH_CANVAS_ITEMS);
    }

    @Before
    public void before() {
        cleanUp();
    }

    @Test
    public void test() {
        UUID canvas1 = UUID.randomUUID();
        DBVirtualStylistCanvasItem item1 = createItem(UUID.randomUUID(), canvas1, "1Test", 100, 100, true);
        DBVirtualStylistCanvasItem item2 = createItem(UUID.randomUUID(), canvas1, "2Test", 200, 200, false);
        DBVirtualStylistCanvasItem item3 = createItem(UUID.randomUUID(), canvas1, "3Test", 300, 300, true);

        itemDAO.save(item1);
        itemDAO.save(item2);
        itemDAO.save(item3);

        check(item1, itemDAO.getById(item1.getId()));
        check(item1, itemDAO.getBySKU(item1.getSkuCode()));
    }

    private DBVirtualStylistCanvasItem createItem(UUID id, UUID canvasId, String sku, int posX, int posY, boolean mainObject) {
        DBVirtualStylistCanvasItem item = new DBVirtualStylistCanvasItem();
        item.setId(id);
        item.setCanvasId(canvasId);
        item.setPositionX(posX);
        item.setPositionY(posY);
        item.setSkuCode(sku);
        return item;
    }

    private void check(DBVirtualStylistCanvasItem item1, DBVirtualStylistCanvasItem item2) {
        assertThat(item1.getId(), equalTo(item2.getId()));
        assertThat(item1.getCanvasId(), equalTo(item2.getCanvasId()));
        assertThat(item1.getSkuCode(), equalTo(item2.getSkuCode()));
        assertThat(item1.getPositionX(), equalTo(item2.getPositionX()));
        assertThat(item1.getPositionY(), equalTo(item2.getPositionY()));
    }
}
