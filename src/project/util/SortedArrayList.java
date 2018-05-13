package project.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Classe générique implémentant une ArrayList triée.
 * @author Jimenez
 */
@SuppressWarnings("serial") // l'attribut serialVersionUID n'est utile que si on prévoit de sérialiser la classe
public class SortedArrayList<T> extends ArrayList<T>{
    private final ArrayList<T> array;
    private Comparator<? super T> comparator;
    /**
     * Construit un SortedArray vide obéissant à la règle de tri spécifiée grâce au paramètre cmp.
     * @param cmp la règle de tri que l'instance doit suivre.
     */
    public SortedArrayList(Comparator<T> cmp){
        array = new ArrayList<>();
        comparator = cmp;
    }
    @Override
    public Iterator<T> iterator() {
        return array.iterator();
    }
    @Override
    public int size() {
        return array.size();
    }
    @Override
    public boolean isEmpty() {
        return array.isEmpty();
    }
    @Override
    public boolean contains(Object o) {
       return array.contains(o);
    }
    @Override
    public Object[] toArray() {
        return array.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return array.toArray(a);
    }

    @Override
    public boolean add(T e) {
        int i = Collections.binarySearch(array, e, comparator);
        i = i > 0 ? i : 0;
        array.add(i, e);
        return true;
    }
    
    @Override
    public T get(int index){
        return array.get(index);
    }

    @Override
    public boolean remove(Object o) {
        return array.remove(o); // à priori l'ordre devrait être préservé
    }
    
    @Override
    public boolean containsAll(Collection<?> c) {
        return array.containsAll(c);
    }
    
    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean ok = array.addAll(c);
        array.sort(comparator);
        return ok;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return array.removeAll(c); // idem, l'ordre devrait être bon
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return array.retainAll(c);
    }

    @Override
    public void clear() {
        array.clear();
    }

    public ArrayList<T> getArray(){
        return array;
    }
    public Comparator<?> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<? super T> comparator) {
        this.comparator = comparator;
    }
   
}
