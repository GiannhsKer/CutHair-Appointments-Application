-- Add audit columns to appointments table
ALTER TABLE appointments 
ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP;

-- Update existing records to have updated_at set to created_at
UPDATE appointments SET updated_at = created_at WHERE updated_at IS NULL;

-- Make updated_at NOT NULL after setting default values
ALTER TABLE appointments ALTER COLUMN updated_at SET NOT NULL; 