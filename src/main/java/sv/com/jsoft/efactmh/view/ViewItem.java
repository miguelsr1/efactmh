package sv.com.jsoft.efactmh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import sv.com.jsoft.efactmh.model.ItemDto;

/**
 *
 * @author migue
 */
@Named
@ViewScoped
public class ViewItem implements Serializable {

    @Getter
    @Setter
    private ItemDto itemDto;
    @Getter
    private List<ItemDto> lstItems;

    {
        itemDto = new ItemDto();
        lstItems = new ArrayList();
    }

    public void addItem() {
        lstItems.add(itemDto);
        itemDto = new ItemDto();
    }
}
