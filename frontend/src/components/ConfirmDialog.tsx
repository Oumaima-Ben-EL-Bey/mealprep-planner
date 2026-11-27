interface ConfirmDialogProps {
    title: string;
    message: string;
    onCancel: () => void;
    onConfirm: () => void;
}

function ConfirmDialog({ title, message, onCancel, onConfirm }: ConfirmDialogProps) {
    return (
        <div className="confirm-dialog-overlay">
            <div className="confirm-dialog">
                <h2>{title}</h2>
                <p>{message}</p>

                <div className="confirm-dialog-actions">
                    <button className="btn btn-secondary" onClick={onCancel}>
                        Abbrechen
                    </button>

                    <button className="btn btn-danger" onClick={onConfirm}>
                        Löschen
                    </button>
                </div>
            </div>
        </div>
    );
}

export default ConfirmDialog;