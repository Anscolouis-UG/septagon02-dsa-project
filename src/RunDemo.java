import datastructures.CustomArrayList;

public class RunDemo {
    public static void main(String[] args) {
        CustomArrayList<Integer> list = new CustomArrayList<>();
        list.add(10);
        list.add(20);
        list.add(30);
        System.out.println("size=" + list.size());
        System.out.println("get(1)=" + list.get(1));
        list.insert(1, 25);
        System.out.println("after insert=" + list.get(1));
        System.out.println("removed=" + list.remove(1));
        System.out.println("final size=" + list.size());
    }
}
