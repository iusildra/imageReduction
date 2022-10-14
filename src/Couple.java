import java.util.Objects;


public class Couple <T,K> {

    private T element1;
    private K element2;

    public Couple(T element1, K element2) {
        this.element1 = element1;
        this.element2 = element2;
    }

    public Couple(Couple<T,K> c) {
        this(c.element1,c.element2);
    }
    
    public void setElement1(T element1) {
        this.element1 = element1;
    }

    public void setElement2(K element2) {
        this.element2 = element2;
    }

    public T getElement1() {
        return element1;
    }

    public K getElement2() {
        return element2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Couple<?, ?> couple = (Couple<?, ?>) o;
        return element1.equals(((Couple<?, ?>) o).element1) &&
                element2.equals(((Couple<?, ?>) o).element2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(element1, element2);
    }


 public String toString(){
	return "(" + element1 + ","+ element2+")";	
}
}
