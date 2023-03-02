package org.noopi.utils.machine;

public enum MachineAction {
  TAPE_RIGHT("right"),
  TAPE_LEFT("left"),
  MACHINE_STOP("stop");

  private final String r;

  MachineAction(String r) {
    this.r = r;
  }

  @Override
  public String toString() {
    return r;
  }
}
