import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Button, Dialog, Grid, GridColumn, GridItemModel, TextField, VerticalLayout } from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';
import { useDataProvider } from '@vaadin/hilla-react-crud';
import { GeneroService } from 'Frontend/generated/endpoints';
import { useEffect } from 'react';
import Genero from 'Frontend/generated/com/unl/clasesestructura/base/models/Genero';


export const config: ViewConfig = {
  title: 'Género',
  menu: {
    icon: 'vaadin:clipboard-check',
    order: 2,
    title: 'Género',
  },
};

type GeneroEntryFormProps = {
  onGeneroCreated?: () => void;
};

type GeneroEntryFormUpdateProps = {
  onGeneroUpdate: () => void;
  arguments: {
    id: string;
    nombre: string;
  };
};

function GeneroEntryForm(props: GeneroEntryFormProps) {
  const dialogOpened = useSignal(false);
  const nombre = useSignal('');

  const open = () => {
    dialogOpened.value = true;
  };

  const close = () => {
    dialogOpened.value = false;
  };

  const createGenero = async () => {
    try {
      if (nombre.value.trim().length > 0) {
        await GeneroService.createGenero(nombre.value);

        if (props.onGeneroCreated) {
          props.onGeneroCreated();
        }
        nombre.value = '';
        dialogOpened.value = false;
        Notification.show('Género creado exitosamente', { 
          duration: 5000, 
          position: 'bottom-end', 
          theme: 'success' 
        });
      } else {
        Notification.show('No se pudo crear, falta el nombre', { 
          duration: 5000, 
          position: 'top-center', 
          theme: 'error' 
        });
      }
    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        aria-label="Registrar Género"
        draggable
        modeless
        opened={dialogOpened.value}
        onOpenedChanged={(event) => {
          dialogOpened.value = event.detail.value;
        }}
        header={
          <h2 className="draggable" style={{
            flex: 1,
            cursor: 'move',
            margin: 0,
            fontSize: '1.5em',
            fontWeight: 'bold',
            padding: 'var(--lumo-space-m) 0',
          }}>
            Registrar Género
          </h2>
        }
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={createGenero}>
              Registrar
            </Button>
          </>
        )}
      >
        <VerticalLayout
          theme="spacing"
          style={{ width: '300px', maxWidth: '100%', alignItems: 'stretch' }}
        >
          <TextField 
            label="Nombre"
            placeholder='Ingrese el nombre del género'
            aria-label='Ingrese el nombre del género'
            value={nombre.value}
            onValueChanged={(evt) => (nombre.value = evt.detail.value)}
          />
        </VerticalLayout>
      </Dialog>
      <Button onClick={open}>Registrar</Button>
    </>
  );
}

function GeneroEntryFormUpdate(props: GeneroEntryFormUpdateProps) {
  const dialogOpened = useSignal(false);
  const nombre = useSignal(props.arguments.nombre);
  const id = useSignal(props.arguments.id);

  // Sincroniza los valores cuando cambian las props
  useEffect(() => {
    nombre.value = props.arguments.nombre;
    id.value = props.arguments.id;
  }, [props.arguments.nombre, props.arguments.id]);

  const open = () => {
    dialogOpened.value = true;
  };

  const close = () => {
    dialogOpened.value = false;
  };

  const updateGenero = async () => {
    try {
      if (nombre.value.trim().length > 0) {
        await GeneroService.updateGenero(Number(id.value), nombre.value);

        if (props.onGeneroUpdate) {
          props.onGeneroUpdate();
        }
        dialogOpened.value = false;
        Notification.show('Género actualizado exitosamente', { 
          duration: 5000, 
          position: 'bottom-end', 
          theme: 'success' 
        });
      } else {
        Notification.show('No se pudo actualizar, falta el nombre', { 
          duration: 5000, 
          position: 'top-center', 
          theme: 'error' 
        });
      }
    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        aria-label="Editar Género"
        draggable
        modeless
        opened={dialogOpened.value}
        onOpenedChanged={(event) => {
          dialogOpened.value = event.detail.value;
        }}
        header={
          <h2 className="draggable" style={{
            flex: 1,
            cursor: 'move',
            margin: 0,
            fontSize: '1.5em',
            fontWeight: 'bold',
            padding: 'var(--lumo-space-m) 0',
          }}>
            Editar Género
          </h2>
        }
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={updateGenero}>
              Actualizar
            </Button>
          </>
        )}
      >
        <VerticalLayout
          theme="spacing"
          style={{ width: '300px', maxWidth: '100%', alignItems: 'stretch' }}
        >
          <TextField 
            label="Nombre"
            placeholder='Ingrese el nombre del género'
            aria-label='Ingrese el nombre del género'
            value={nombre.value}
            onValueChanged={(evt) => (nombre.value = evt.detail.value)}
          />
        </VerticalLayout>
      </Dialog>
      <Button onClick={open}>Editar</Button>
    </>
  );
}

function index({ model }: { model: GridItemModel<any> }) {
  return <span>{model.index + 1}</span>;
}

function link({ item }: { item: any }) {
  return (
    <span>
      <GeneroEntryFormUpdate 
        arguments={{ id: item.id, nombre: item.nombre }} 
        onGeneroUpdate={dataProvider.refresh} 
      />
    </span>
  );
}

export default function GeneroListView() {
  const dataProvider = useDataProvider<Genero>({
    list: () => GeneroService.listAllGenero(),
  });

  function index({ model }: { model: GridItemModel<any> }) {
    return <span>{model.index + 1}</span>;
  }

  function link({ item }: { item: any }) {
    return (
      <span>
        <GeneroEntryFormUpdate 
          arguments={{ id: item.id, nombre: item.nombre }} 
          onGeneroUpdate={dataProvider.refresh} 
        />
      </span>
    );
  }

  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m">
      <ViewToolbar title="Géneros">
        <Group>
          <GeneroEntryForm onGeneroCreated={dataProvider.refresh} />
        </Group>
      </ViewToolbar>
      <Grid dataProvider={dataProvider.dataProvider}>
        <GridColumn header="Nro" renderer={index} />
        <GridColumn path="nombre" header="Nombre del género" />
        <GridColumn header="Acciones" renderer={link} />
      </Grid>
    </main>
  );
}