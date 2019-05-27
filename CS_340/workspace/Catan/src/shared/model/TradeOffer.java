package shared.model;

/**
 * Created by Home on 9/18/2016.
 */
public class TradeOffer {

    private int _sender;
    private int _reciever;
    private ResourceList _offer;

    public TradeOffer(){}

    public int get_sender() {
        return _sender;
    }

    public void set_sender(int _sender) {
        this._sender = _sender;
    }

    public int get_reciever() {
        return _reciever;
    }

    public void set_reciever(int _reciever) {
        this._reciever = _reciever;
    }

    public ResourceList get_offer() {
        return _offer;
    }

    public void set_offer(ResourceList _offer) {
        this._offer = _offer;
    }
}
