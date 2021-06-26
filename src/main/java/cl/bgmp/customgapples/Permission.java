package cl.bgmp.customgapples;

public interface Permission {
  String NODE = "customgapples";

  String GAPPLE_GET_COMMAND = NODE + ".command.gapple.get";
  String GAPPLE_GIVE_COMMAND = NODE + ".command.gapple.give";
  String GAPPLE_LIST_COMMAND = NODE + ".command.gapple.list";
  String GAPPLE_RELOAD_COMMAND = NODE + ".command.gapple.reload";
}
