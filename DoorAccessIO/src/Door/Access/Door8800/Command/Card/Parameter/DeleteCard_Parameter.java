package Door.Access.Door8800.Command.Card.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 删除卡片
 * @author 赖金杰
 */
public class DeleteCard_Parameter extends CommandParameter {

    /**
     * 需要删除的卡片列表
     */
    public String[] CardList;

    /**
     * 删除卡片
     * @param detail
     * @param list
     */
    public DeleteCard_Parameter(CommandDetail detail, String[] list) {
        super(detail);
        CardList = list;
    }

}
