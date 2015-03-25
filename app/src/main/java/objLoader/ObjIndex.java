package objLoader;

/**
 * Created by Alessandro on 16/03/15.
 */
public class ObjIndex implements Comparable<ObjIndex> {

    private int vIndex;
    private int vnIndex;
    private int vtIndex;

    public ObjIndex(int vIndex, int vtIndex, int vnIndex) {
        super();
        this.vIndex = vIndex;
        this.vnIndex = vnIndex;
        this.vtIndex = vtIndex;
    }

    public ObjIndex() {

    }

    public void set(ObjIndex set) {
        this.vIndex = set.vIndex;
        this.vnIndex = set.vnIndex;
        this.vtIndex = set.vtIndex;
    }

    @Override
    public int compareTo(ObjIndex o) {
        int compare = vIndex - o.vIndex;
        if (compare == 0) {
            compare = vnIndex - o.vnIndex;
            if (compare == 0) {
                compare = vtIndex - o.vtIndex;
            }
        }
        return compare;
    }

    public void subtract(ObjIndex set) {
        vIndex -= set.vIndex;
        vnIndex -= set.vnIndex;
        vtIndex -= set.vtIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ObjIndex) {
            return compareTo(((ObjIndex) obj)) == 0;
        }
        return super.equals(obj);
    }

    public int getvIndex() {
        return vIndex;
    }

    public int getVnIndex() {
        return vnIndex;
    }

    public int getVtIndex() {
        return vtIndex;
    }

    public void setvIndex(int vIndex) {
        this.vIndex = vIndex;
    }

    public void setVnIndex(int vnIndex) {
        this.vnIndex = vnIndex;
    }

    public void setVtIndex(int vtIndex) {
        this.vtIndex = vtIndex;
    }
}
