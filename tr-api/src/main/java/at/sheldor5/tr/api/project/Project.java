package at.sheldor5.tr.api.project;

public class Project {

  private int id;
  private String name;
  private Project parent;

  public Project() {
    this(null, null);
  }

  public Project(final String name) {
    this(name, null);
  }

  public Project(final String name, final Project parent) {
    this.name = name;
    this.parent = parent;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Project getParent() {
    return parent;
  }

  public void setParent(Project parent) {
    this.parent = parent;
  }

  @Override
  public boolean equals(final Object obj) {
    if (super.equals(obj)) {
      return true;
    }
    if (name == null || obj == null || !(obj instanceof Project)) {
      return false;
    }

    Project project = (Project) obj;
    return name.equals(project.name);
  }

}
