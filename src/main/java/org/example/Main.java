package lab4;

interface CostCalculator {
    double calculate(double weight, double distance);
}

interface DeliveryMethod {
    void deliver(String orderId);
}

interface Trackable {
    String getTrackingStatus(String orderId);
}

abstract class AbstractDelivery implements DeliveryMethod {
    protected String deliveryName;

    public AbstractDelivery(String deliveryName) {
        this.deliveryName = deliveryName;
    }
    protected void logStart(String orderId) {
        System.out.println("[" + deliveryName + "] Початок обробки замовлення №" + orderId);
    }
}

class StandardCostCalculator implements CostCalculator {
    @Override
    public double calculate(double weight, double distance) {
        return (weight * 5) + (distance * 2);
    }
}

class PremiumCostCalculator implements CostCalculator {
    @Override
    public double calculate(double weight, double distance) {
        return (weight * 5) + (distance * 2) + 150;
}

class PickupDelivery extends AbstractDelivery {
    public PickupDelivery() {
        super("Самовивіз");
    }

    @Override
    public void deliver(String orderId) {
        logStart(orderId);
        System.out.println("Замовлення №" + orderId + " очікує клієнта на точці видачі.");
    }
}

class PostDelivery extends AbstractDelivery implements Trackable {
    public PostDelivery() {
        super("Пошта");
    }

    @Override
    public void deliver(String orderId) {
        logStart(orderId);
        System.out.println("Замовлення №" + orderId + " передано у поштове відділення.");
    }

    @Override
    public String getTrackingStatus(String orderId) {
        return "Посилка №" + orderId + " прямує до вашого міста.";
    }
}

class CourierDelivery extends AbstractDelivery implements Trackable {
    public CourierDelivery() {
        super("Кур'єр");
    }

    @Override
    public void deliver(String orderId) {
        logStart(orderId);
        System.out.println("Замовлення №" + orderId + " передано кур'єру.");
    }

    @Override
    public String getTrackingStatus(String orderId) {
        return "Кур'єр із замовленням №" + orderId + " буде у вас через 15 хвилин.";
    }
}

class DeliveryService {
    private final DeliveryMethod deliveryMethod;
    private final CostCalculator costCalculator;

    public DeliveryService(DeliveryMethod deliveryMethod, CostCalculator costCalculator) {
        this.deliveryMethod = deliveryMethod;
        this.costCalculator = costCalculator;
    }

    public void processOrder(String orderId, double weight, double distance) {
        System.out.println("--- Оформлення замовлення ---");
        double cost = costCalculator.calculate(weight, distance);
        System.out.println("Вартість доставки: " + cost + " грн");

        deliveryMethod.deliver(orderId);

        if (deliveryMethod instanceof Trackable) {
            Trackable trackableDelivery = (Trackable) deliveryMethod;
            System.out.println("Відстеження: " + trackableDelivery.getTrackingStatus(orderId));
        }
        System.out.println("-----------------------------");
    }
}

class DroneDelivery extends AbstractDelivery implements Trackable {
    public DroneDelivery() {
        super("Дрон");
    }

    @Override
    public void deliver(String orderId) {
        logStart(orderId);
        System.out.println("Дрон вилетів із замовленням №" + orderId + ".");
    }

    @Override
    public String getTrackingStatus(String orderId) {
        return "Дрон у повітрі. GPS координати: 48.450, 35.045.";
    }
}

public class Main {
    public static void main(String[] args) {
        DeliveryService pickupService = new DeliveryService(new PickupDelivery(), new StandardCostCalculator());
        pickupService.processOrder("101", 2.5, 0);

        DeliveryService postService = new DeliveryService(new PostDelivery(), new StandardCostCalculator());
        postService.processOrder("102", 5.0, 300);

        DeliveryService courierService = new DeliveryService(new CourierDelivery(), new PremiumCostCalculator());
        courierService.processOrder("103", 1.2, 10);

        System.out.println("\n*** Демонстрація розширення системи (Нова реалізація) ***");
        DeliveryService droneService = new DeliveryService(new DroneDelivery(), new PremiumCostCalculator());
        droneService.processOrder("104", 0.5, 5);
    }
}