/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Command;

/**
 *
 * @author 赖金杰
 */
public class CommandParameter implements INCommandParameter {

    protected CommandDetail _Detail;

    public CommandParameter(CommandDetail detail) {
        _Detail = detail;
    }

    @Override
    public CommandDetail getCommandDetail() {
        return _Detail;
    }

    @Override
    public void setCommandDetail(CommandDetail detail) {
        _Detail = detail;
    }

}
